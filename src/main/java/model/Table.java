package main.java.model;


import main.java.model.enums.TableStatus;

/**
 * @author MnhTng
 * @Package Models
 * @date 4/16/2025 6:00 PM
 * @Copyright tùng
 */

public class Table {
    private int id;
    private int seatCount;
    private TableStatus status;

    public Table() {}

    public Table(int id, int seatCount, TableStatus status) {
        this.id = id;
        this.seatCount = seatCount;
        this.status = status;
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
}
