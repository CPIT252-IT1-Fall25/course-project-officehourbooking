package sa.edu.kau.fcit.cpit252.project.booking;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public class BookingRequest {

    @NotNull
    private Long doctorId;

    @NotNull
    private Long studentId;

    @NotNull
    private LocalDateTime startTime;

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
