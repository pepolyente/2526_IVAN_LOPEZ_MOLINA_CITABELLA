package com.citabella.citabellaapi.repository;

import com.citabella.citabellaapi.entity.appointment.AppointmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentSubmissionRepository extends JpaRepository<AppointmentSubmission, Integer> {
}
