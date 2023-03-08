package pl.kurs.watercontainters.domain;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Container {
    private String name;
    private double maxCapacity;
    private double waterLevel;
    private List<OperationEvent> operationEventsHistory;


    private Container(String name, double maxCapacity, double waterLevel) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.waterLevel = waterLevel;
        operationEventsHistory = new ArrayList<>();
    }

    public static Container create(String name, double maxCapacity){
        if (maxCapacity <= 0)
            throw new RuntimeException("maxCapacity should be above 0!");
        return new Container(name, maxCapacity, 0);
    }

    public void addWater(double value) {
        boolean success;
        if (addingWaterIsPossible(value)) {
            waterLevel += value;
            success = true;
        } else {
            System.out.println("You trying add too much water!");
            success = false;
        }
        saveEvent(new OperationEvent(Timestamp.from(Instant.now()), this, OperationEvent.OperationType.ADD, value, success));
    }

    public void pourWater(Container source, double value) {
        if (source.drainingWaterIsPossible(value) && this.addingWaterIsPossible(value)) {
            source.drainWater(value);
            this.addWater(value);
        }

    }

    public void drainWater(double value) {
        boolean success;
        if (drainingWaterIsPossible(value)) {
            waterLevel -= value;
            success = true;
        } else {
            System.out.println("You trying drain too much water!");
            success = false;
        }
        saveEvent(new OperationEvent(Timestamp.from(Instant.now()), this, OperationEvent.OperationType.DRAIN, value, success));

    }

    private void saveEvent(OperationEvent operationEvent) {
        operationEventsHistory.add(operationEvent);
    }

    private boolean addingWaterIsPossible(double value) {
        return waterLevel + value <= maxCapacity;
    }

    private boolean drainingWaterIsPossible(double value) {
        return waterLevel - value >= 0;
    }

    public List<OperationEvent> getOperationEventsHistory() {
        return operationEventsHistory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(double maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public double getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(double waterLevel) {
        this.waterLevel = waterLevel;
    }

    @Override
    public String toString() {
        return "Container{" +
                "name='" + name + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", waterLevel=" + waterLevel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Container container = (Container) o;
        return Double.compare(container.maxCapacity, maxCapacity) == 0 && Double.compare(container.waterLevel, waterLevel) == 0 && Objects.equals(name, container.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, maxCapacity, waterLevel);
    }
}
