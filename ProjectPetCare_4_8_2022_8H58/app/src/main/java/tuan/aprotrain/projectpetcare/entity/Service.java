package tuan.aprotrain.projectpetcare.entity;

import java.sql.Time;

public class Service {
    private long serviceId;
    private String serviceName;
    private String categoryId;
    private Float servicePrice;//sua thanh servicePrice
    private long serviceTime;//Them serviceTime
    public static String TABLE_NAME = "Services";//them table name

    public Service(){}

    public Service(long serviceId, String serviceName, String categoryId, Float servicePrice, long serviceTime) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.categoryId = categoryId;
        this.servicePrice = servicePrice;
        this.serviceTime = serviceTime;
    }
    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Float getServicePrice() {return servicePrice;}

    public void setServicePrice(Float servicePrice) {this.servicePrice = servicePrice;}

    public long getServiceTime() {return serviceTime;}

    public void setServiceTime(long serviceTime) {this.serviceTime = serviceTime;}
}
