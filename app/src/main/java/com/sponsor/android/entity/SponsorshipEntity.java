package com.sponsor.android.entity;

import java.io.Serializable;

public class SponsorshipEntity implements Serializable{
    private String id;
    private String name;
    private int sponsor_id;
    private String sponsor_name;
    private Integer team_id;
    private String intro;
    private String application_start_date;
    private String application_end_date;
    private String application_condition;
    private int status;
    private String updated_at;
    private int apply_status = -1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSponsorId() {
        return sponsor_id;
    }

    public void setSponsor_id(int sponsor_id) {
        this.sponsor_id = sponsor_id;
    }

    public String getSponsorName() {
        return sponsor_name;
    }

    public void setSponsor_name(String sponsor_name) {
        this.sponsor_name = sponsor_name;
    }

    public Integer getTeamId() {
        return team_id;
    }

    public void setTeam_id(Integer team_id) {
        this.team_id = team_id;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getApplicationStartDate() {
        return application_start_date;
    }

    public void setApplication_start_date(String application_start_date) {
        this.application_start_date = application_start_date;
    }

    public String getApplicationEndDate() {
        return application_end_date;
    }

    public void setApplication_end_date(String application_end_date) {
        this.application_end_date = application_end_date;
    }

    public String getApplicationCondition() {
        return application_condition;
    }

    public void setApplicationCondition(String application_condition) {
        this.application_condition = application_condition;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getApplyStatus() {
        return apply_status;
    }

    public void setApply_status(int apply_status) {
        this.apply_status = apply_status;
    }

    public boolean isApplied() {
        return this.apply_status > -1;
    }

    public boolean isPendingApply() {
        return this.apply_status == 0;
    }

    public boolean isApprovedApply() {
        return this.apply_status == 1;
    }

    public boolean isRejectedApply() {
        return this.apply_status == 2;
    }
}
