package com.airtribe.lms.logistic.service;

import com.airtribe.lms.constants.AppConstants;
import com.airtribe.lms.model.BookCopy;
import com.airtribe.lms.model.Branch;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LogisticsService {
	
	private final Map<String, Branch> branchNetwork = new ConcurrentHashMap<>();

    public void registerBranch(Branch branch) {
        branchNetwork.put(branch.getBranchId(), branch);
    }

    /**
     * Workflow: Prepares a book copy at a source branch for transit.
     * Prevents local users from checking it out while it is on the move.
     */
    public boolean initiateTransit(String copyId, String sourceBranchId) {
        Branch sourceBranch = branchNetwork.get(sourceBranchId);
        if (sourceBranch == null) return false;

        BookCopy copy = sourceBranch.getCopy(copyId);
        if (copy != null && AppConstants.AVAILABLE.equalsIgnoreCase(copy.getStatus())) {
            copy.setStatus(AppConstants.IN_TRANSIT);
            return true;
        }
        return false;
    }

    /**
     * Workflow: Processes a book arriving in a courier box at the target branch.
     */
    public boolean receiveTransit(String copyId, String sourceBranchId, String targetBranchId) {
        Branch source = branchNetwork.get(sourceBranchId);
        Branch target = branchNetwork.get(targetBranchId);

        if (source == null || target == null) return false;

        // Remove from source inventory map completely
        BookCopy movingCopy = source.removeCopy(copyId);
        
        if (movingCopy != null && AppConstants.IN_TRANSIT.equalsIgnoreCase(movingCopy.getStatus())) {
            movingCopy.setCurrentBranchId(targetBranchId);
            movingCopy.setStatus(AppConstants.AVAILABLE);
            
            // Add to target branch inventory map
            target.addCopy(movingCopy);
            return true;
        }
        return false;
    }
}