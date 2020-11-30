package com.example.a2105projectgroup13;

public class Review {
    private String comment;
    private int rating;
    private Branch branchReviewed;
    private Customer reviewer;

    public Review(){
    }

    public Review (String comment, int rating, Branch branchReviewed, Customer reviewer){
        this.comment = comment;
        this.rating = rating;
        this.branchReviewed = branchReviewed;
        this.reviewer = reviewer;
    }

    public String getComment(){
        return comment;
    }

    public int getRating(){
        return rating;
    }

    public Branch getBranchReviewed(){
        return branchReviewed;
    }

    public Customer getReviewer(){
        return reviewer;
    }

    public String toString(){
        return getReviewer().getFirstName() + " " + getReviewer().getLastName() + " wrote:" + "\n" + "  Rating: " + getRating() + "/5" +
                "\n" + "    Comment: " + getComment();
    }

}
