package com.codeit.sb06deokhugamteam2.dashboard.repository;

import com.codeit.sb06deokhugamteam2.dashboard.entity.DashBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DashBoardRepository extends JpaRepository<DashBoard, UUID>, DashBoardRepositoryCustom {
}
