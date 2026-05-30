package com.airtribe.lms.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Branch {
    private final String branchId;
    private final String branchName;
    private final Map<String, BookCopy> localInventory = new ConcurrentHashMap<>();

    public Branch(String branchId, String branchName) {
        this.branchId = branchId;
        this.branchName = branchName;
    }

    public void addCopy(BookCopy copy) {
        localInventory.put(copy.getCopyId(), copy);
    }

    public BookCopy getCopy(String copyId) {
        return localInventory.get(copyId);
    }

    public BookCopy removeCopy(String copyId) {
        return localInventory.remove(copyId);
    }

    public String getBranchId() { return branchId; }
    public String getBranchName() { return branchName; }
    public Map<String, BookCopy> getLocalInventory() { return localInventory; }
}