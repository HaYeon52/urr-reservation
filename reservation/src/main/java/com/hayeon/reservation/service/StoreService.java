package com.hayeon.reservation.service;

import com.hayeon.reservation.common.BusinessException;
import com.hayeon.reservation.dto.store.MenuResponse;
import com.hayeon.reservation.dto.store.MinOrderRuleResponse;
import com.hayeon.reservation.dto.store.SlotDetailResponse;
import com.hayeon.reservation.dto.store.SlotTimelineItemResponse;
import com.hayeon.reservation.dto.store.StoreDetailResponse;
import com.hayeon.reservation.dto.store.StoreInfoResponse;
import com.hayeon.reservation.dto.store.StoreListItemResponse;
import com.hayeon.reservation.entity.Menu;
import com.hayeon.reservation.entity.MinOrderRule;
import com.hayeon.reservation.entity.Slot;
import com.hayeon.reservation.entity.Store;
import com.hayeon.reservation.repository.MenuRepository;
import com.hayeon.reservation.repository.MinOrderRuleRepository;
import com.hayeon.reservation.repository.SlotRepository;
import com.hayeon.reservation.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final SlotRepository slotRepository;
    private final MenuRepository menuRepository;
    private final MinOrderRuleRepository minOrderRuleRepository;

    @Transactional(readOnly = true)
    public List<StoreListItemResponse> listStores(LocalDate date, int headcount) {
        List<Store> stores = storeRepository.findByMaxCapacityGreaterThanEqual(headcount);
        return stores.stream()
                .sorted(Comparator.comparing(Store::getStoreId))
                .map(store -> {
                    List<Slot> slots = slotRepository.findByStore_StoreIdAndSlotDate(store.getStoreId(), date);
                    List<SlotTimelineItemResponse> timeline = slots.stream()
                            .sorted(Comparator.comparing(Slot::getTimeBlock))
                            .map(s -> new SlotTimelineItemResponse(
                                    s.getSlotId(),
                                    s.getTimeBlock(),
                                    Boolean.TRUE.equals(s.getIsAvailable()),
                                    s.getMaxPeople()
                            ))
                            .collect(Collectors.toList());
                    return new StoreListItemResponse(
                            store.getStoreId(),
                            store.getName(),
                            store.getMaxCapacity(),
                            store.getImageUrl(),
                            timeline
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StoreDetailResponse getStoreDetail(String storeId, LocalDate date) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다."));

        List<Slot> slots = slotRepository.findByStore_StoreIdAndSlotDate(storeId, date);
        List<SlotDetailResponse> slotDtos = slots.stream()
                .sorted(Comparator.comparing(Slot::getTimeBlock))
                .map(s -> new SlotDetailResponse(
                        s.getSlotId(),
                        s.getSlotDate(),
                        s.getTimeBlock(),
                        Boolean.TRUE.equals(s.getIsAvailable()),
                        s.getMaxPeople()
                ))
                .collect(Collectors.toList());

        List<Menu> menus = menuRepository.findByStore_StoreId(storeId);
        List<MenuResponse> menuDtos = menus.stream()
                .sorted(Comparator.comparing(Menu::getMenuId))
                .map(m -> new MenuResponse(
                        m.getMenuId(),
                        m.getName(),
                        m.getPrice(),
                        Boolean.TRUE.equals(m.getIsRequired())
                ))
                .collect(Collectors.toList());

        List<MinOrderRule> rules = minOrderRuleRepository.findByStore_StoreIdOrderByMinHeadcountAsc(storeId);
        List<MinOrderRuleResponse> ruleDtos = rules.stream()
                .map(r -> new MinOrderRuleResponse(
                        r.getMinHeadcount(),
                        r.getMaxHeadcount(),
                        r.getMinOrderAmount()
                ))
                .collect(Collectors.toList());

        StoreInfoResponse info = new StoreInfoResponse(
                store.getStoreId(),
                store.getName(),
                store.getMaxCapacity(),
                store.getImageUrl()
        );

        return new StoreDetailResponse(info, slotDtos, menuDtos, ruleDtos);
    }
}
