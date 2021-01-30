package com.autodokta.app.Models;

public class JobsModel {
    String jobID;
    String title;
    String description;
    String experience;
    String job_type;
    String regions;
    String responsibilities;


    public JobsModel(String jobID,              String title,
                     String description,        String experience,
                     String job_type,           String regions,
                     String responsibilities) {

        this.jobID              =   jobID;
        this.title              =   title;
        this.description        =   description;
        this.experience         =   experience;
        this.job_type           =   job_type;
        this.regions            =   regions;
        this.responsibilities   =   responsibilities;
    }

    public String getJobID() {
        return jobID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getExperience() {
        return experience;
    }

    public String getJob_type() {
        return job_type;
    }

    public String getRegions() {
        return regions;
    }

    public String getResponsibilities() {
        return responsibilities;
    }
}

