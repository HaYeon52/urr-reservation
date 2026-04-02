package com.hayeon.reservation.service;

import com.hayeon.reservation.common.BusinessException;
import com.hayeon.reservation.dto.admin.AdminMenuItemResponse;
import com.hayeon.reservation.dto.admin.AdminReservationListItemResponse;
import com.hayeon.reservation.dto.reservation.CancelReservationResponse;
import com.hayeon.reservation.dto.reservation.CreateReservationRequest;
import com.hayeon.reservation.dto.reservation.CreateReservationResponse;
import com.hayeon.reservation.dto.reservation.ReservationCheckResponse;
import com.hayeon.reservation.dto.reservation.ReservationMenuCheckResponse;
import com.hayeon.reservation.dto.reservation.SelectedMenuRequest;
import com.hayeon.reservation.entity.Menu;
import com.hayeon.reservation.entity.MinOrderRule;
import com.hayeon.reservation.entity.Reservation;
import com.hayeon.reservation.entity.ReservationMenu;
import com.hayeon.reservation.entity.Slot;
import com.hayeon.reservation.entity.Store;
import com.hayeon.reservation.repository.MenuRepository;
import com.hayeon.reservation.repository.MinOrderRuleRepository;
import com.hayeon.reservation.repository.ReservationMenuRepository;
import com.hayeon.reservation.repository.ReservationRepository;
import com.hayeon.reservation.repository.SlotRepository;
import com.hayeon.reservation.repository.StoreRepository;
import com.hayeon.reservation.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private static final String STATUS_CONFIRMED = "CONFIRMED";
    private static final String STATUS_CANCELED = "CANCELED";

    private final ReservationRepository reservationRepository;
    private final ReservationMenuRepository reservationMenuRepository;
    private final SlotRepository slotRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final MinOrderRuleRepository minOrderRuleRepository;
    private final NotionSyncService notionSyncService;

    @Transactional
    public CreateReservationResponse create(CreateReservationRequest req) {
        String storeId = req.getStoreId().trim();
        String slotId = req.getSlotId().trim();
        String userPhone = normalizePhone(req.getUserPhone());

        storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다."));

        Slot slot = slotRepository.findByIdWithLock(slotId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "슬롯을 찾을 수 없습니다."));

        if (!slot.getStore().getStoreId().equals(storeId)) {
            throw new BusinessException("해당 가게의 슬롯이 아닙니다.");
        }
        if (!Boolean.TRUE.equals(slot.getIsAvailable())) {
            throw new BusinessException("이미 예약된 시간대입니다.");
        }
        if (req.getHeadcount() > slot.getMaxPeople()) {
            throw new BusinessException("인원이 해당 시간대 최대 인원을 초과합니다.");
        }

        List<Menu> storeMenus = menuRepository.findByStore_StoreId(storeId);
        Map<String, Menu> menuById = storeMenus.stream()
                .collect(Collectors.toMap(Menu::getMenuId, m -> m, (a, b) -> a));

        Map<String, Integer> qtyByMenu = new HashMap<>();
        for (SelectedMenuRequest sm : req.getSelectedMenus()) {
            String mid = sm.getMenuId().trim();
            qtyByMenu.merge(mid, sm.getQuantity(), Integer::sum);
        }

        for (String menuId : qtyByMenu.keySet()) {
            if (!menuById.containsKey(menuId)) {
                throw new BusinessException("존재하지 않는 메뉴가 포함되어 있습니다: " + menuId);
            }
        }

        for (Menu m : storeMenus) {
            if (Boolean.TRUE.equals(m.getIsRequired())) {
                int q = qtyByMenu.getOrDefault(m.getMenuId(), 0);
                if (q < 1) {
                    throw new BusinessException("필수 메뉴를 포함해야 합니다: " + m.getName());
                }
            }
        }

        int totalAmount = 0;
        for (Map.Entry<String, Integer> e : qtyByMenu.entrySet()) {
            Menu m = menuById.get(e.getKey());
            totalAmount += m.getPrice() * e.getValue();
        }

        int minOrder = minOrderRuleRepository
                .findRuleByStoreAndHeadcount(storeId, req.getHeadcount())
                .map(MinOrderRule::getMinOrderAmount)
                .orElse(0);

        if (totalAmount < minOrder) {
            throw new BusinessException(
                    String.format("최소 주문 금액(%d원)을 충족하지 않습니다.", minOrder));
        }

        String reservationId = newReservationId();

        Reservation reservation = new Reservation();
        reservation.setReservationId(reservationId);
        reservation.setUserName(req.getUserName().trim());
        reservation.setGroupName(req.getGroupName().trim());
        reservation.setUserPhone(userPhone);
        reservation.setStore(slot.getStore());
        reservation.setSlot(slot);
        reservation.setHeadcount(req.getHeadcount());
        reservation.setTotalAmount(totalAmount);
        reservation.setStatus(STATUS_CONFIRMED);
        String note = req.getUserNote() != null ? req.getUserNote().trim() : null;
        reservation.setUserNote(note != null && !note.isEmpty() ? note : null);
        reservation.setCreatedAt(LocalDateTime.now());

        reservationRepository.save(reservation);

        for (Map.Entry<String, Integer> e : qtyByMenu.entrySet()) {
            Menu m = menuById.get(e.getKey());
            ReservationMenu line = new ReservationMenu();
            line.setId(IdGenerator.randomId13());
            line.setReservation(reservation);
            line.setMenu(m);
            line.setQuantity(e.getValue());
            line.setPriceAtTime(m.getPrice());
            reservationMenuRepository.save(line);
        }

        slot.setIsAvailable(false);
        slotRepository.save(slot);

        notionSyncService.notifyReservationCreated(reservationId);

        return new CreateReservationResponse(
                reservationId,
                STATUS_CONFIRMED,
                totalAmount,
                reservation.getCreatedAt()
        );
    }

    private String newReservationId() {
        for (int i = 0; i < 5; i++) {
            String id = IdGenerator.randomId13();
            if (reservationRepository.findById(id).isEmpty()) {
                return id;
            }
        }
        throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "예약 ID 생성에 실패했습니다.");
    }

    @Transactional
    public CancelReservationResponse cancel(String reservationId) {
        Reservation r = reservationRepository.findById(reservationId.trim())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."));

        if (STATUS_CANCELED.equals(r.getStatus())) {
            throw new BusinessException("이미 취소된 예약입니다.");
        }
        if (!STATUS_CONFIRMED.equals(r.getStatus())) {
            throw new BusinessException("취소할 수 없는 예약 상태입니다.");
        }

        r.setStatus(STATUS_CANCELED);
        Slot slot = r.getSlot();
        slot.setIsAvailable(true);
        slotRepository.save(slot);
        reservationRepository.save(r);

        LocalDateTime now = LocalDateTime.now();
        notionSyncService.notifyReservationCanceled(reservationId);

        return new CancelReservationResponse(reservationId, STATUS_CANCELED, now);
    }

    @Transactional(readOnly = true)
    public List<ReservationCheckResponse> findByPhone(String userPhone) {
        String phone = normalizePhone(userPhone);
        List<Reservation> list = reservationRepository.findByUserPhoneOrderByCreatedAtDesc(phone);
        List<ReservationCheckResponse> out = new ArrayList<>();
        for (Reservation r : list) {
            out.add(toCheckResponse(r));
        }
        return out;
    }

    private ReservationCheckResponse toCheckResponse(Reservation r) {
        List<ReservationMenu> lines = reservationMenuRepository.findByReservation_ReservationId(r.getReservationId());
        List<ReservationMenuCheckResponse> menus = lines.stream()
                .map(l -> new ReservationMenuCheckResponse(
                        l.getMenu().getMenuId(),
                        l.getMenu().getName(),
                        l.getQuantity(),
                        l.getPriceAtTime()
                ))
                .collect(Collectors.toList());

        return new ReservationCheckResponse(
                r.getReservationId(),
                r.getStore().getStoreId(),
                r.getSlot().getSlotId(),
                r.getSlot().getTimeBlock(),
                r.getHeadcount(),
                r.getTotalAmount(),
                r.getStatus(),
                r.getCreatedAt(),
                menus
        );
    }

    @Transactional(readOnly = true)
    public List<AdminReservationListItemResponse> getAdminListConfirmed() {
        List<Reservation> list = reservationRepository.findByStatusOrderByCreatedAtDesc(STATUS_CONFIRMED);
        List<AdminReservationListItemResponse> out = new ArrayList<>();
        for (Reservation r : list) {
            List<ReservationMenu> lines = reservationMenuRepository.findByReservation_ReservationId(r.getReservationId());
            List<AdminMenuItemResponse> items = lines.stream()
                    .map(l -> new AdminMenuItemResponse(
                            l.getMenu().getName(),
                            l.getQuantity(),
                            l.getPriceAtTime()
                    ))
                    .collect(Collectors.toList());
            out.add(new AdminReservationListItemResponse(
                    r.getStore().getStoreId(),
                    r.getSlot().getTimeBlock(),
                    r.getHeadcount(),
                    items,
                    r.getTotalAmount()
            ));
        }
        return out;
    }

    private static String normalizePhone(String raw) {
        if (raw == null) {
            return "";
        }
        return raw.replaceAll("\\s+", "").trim();
    }
}
