package main.java.model;

import main.java.model.enums.TableStatus;

import java.time.LocalDateTime;

public class Table {
    private int id;
    private int seatCount;
    private TableStatus status;
    private LocalDateTime deleteAt;

    public Table() {}

    public Table(int id, int seatCount, TableStatus status, LocalDateTime deleteAt) {
        this.id = id;
        this.seatCount = seatCount;
        this.status = status;
        this.deleteAt = deleteAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    public LocalDateTime getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(LocalDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }
}
