package com.example.a2105projectgroup13;

public class ServiceRequest {
    private String requestNumber;
    private String status;
    private String branchUid;
    private String customerUid;
    private String serviceName;
    private Branch branch;
    private Customer customer;

    public ServiceRequest(){
    }

    public ServiceRequest (String requestNumber, String status, String branchUid, String serviceName, String customerUid){
        this.requestNumber = requestNumber;
        this.status = status;
        this.branchUid = branchUid;
        this.customerUid = customerUid;
        this.serviceName = serviceName;
        this.branch = new Branch();
        this.customer = new Customer();
    }

    public String getRequestNumber(){
        return requestNumber;
    }

    public String getStatus(){
        return status;
    }

    public String getBranchUid(){
        return branchUid;
    }

    public String getCustomerUid(){
        return customerUid;
    }

    public String getServiceName(){
        return serviceName;
    }

    public String toString(){
        return "Branch: " + getBranchUid() + "\n" + getRequestNumber() + "\n" + "Service: " + getServiceName() + "\n" + "Status: " + getStatus();
    }
}
