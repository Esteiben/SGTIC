package com.uteq.sgtic.services.student;


public interface IStudentDegreePeriodService {
    void closePeriod(Integer enrollmentId, boolean approved, String observations);
}   