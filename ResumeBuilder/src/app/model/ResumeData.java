package app.model;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResumeData {
    
    // Personal Information
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    
    // Role and Objective
    private String selectedRole;
    private String objective;
    private String summary;
    
    // Education
    private List<Education> educationList;
    
    // Skills
    private List<String> technicalSkills;
    private List<String> softSkills;
    
    // Projects
    private List<Project> projects;
    
    // Work Experience
    private List<Experience> experiences;
    
    // Achievements
    private List<String> achievements;
    
    // Certifications
    private List<String> certifications;
    
    // Languages
    private List<String> languages;
    
    // Metadata
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
    private String templateSelected;
    
    public ResumeData() {
        this.educationList = new ArrayList<>();
        this.technicalSkills = new ArrayList<>();
        this.softSkills = new ArrayList<>();
        this.projects = new ArrayList<>();
        this.experiences = new ArrayList<>();
        this.achievements = new ArrayList<>();
        this.certifications = new ArrayList<>();
        this.languages = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
    }
    
    // Education nested class
    public static class Education {
        private String degree;
        private String institution;
        private String year;
        private String grade;
        private String location;
        
        public Education() {}
        
        public Education(String degree, String institution, String year, String grade, String location) {
            this.degree = degree;
            this.institution = institution;
            this.year = year;
            this.grade = grade;
            this.location = location;
        }
        
        // Getters and Setters
        public String getDegree() { return degree; }
        public void setDegree(String degree) { this.degree = degree; }
        
        public String getInstitution() { return institution; }
        public void setInstitution(String institution) { this.institution = institution; }
        
        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }
        
        public String getGrade() { return grade; }
        public void setGrade(String grade) { this.grade = grade; }
        
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
    }
    
    // Project nested class
    public static class Project {
        private String title;
        private String description;
        private String technologies;
        private String duration;
        private String githubUrl;
        private String liveUrl;
        
        public Project() {}
        
        public Project(String title, String description, String technologies, String duration) {
            this.title = title;
            this.description = description;
            this.technologies = technologies;
            this.duration = duration;
        }
        
        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getTechnologies() { return technologies; }
        public void setTechnologies(String technologies) { this.technologies = technologies; }
        
        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }
        
        public String getGithubUrl() { return githubUrl; }
        public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }
        
        public String getLiveUrl() { return liveUrl; }
        public void setLiveUrl(String liveUrl) { this.liveUrl = liveUrl; }
    }
    
    // Experience nested class
    public static class Experience {
        private String company;
        private String position;
        private String duration;
        private String location;
        private String description;
        
        public Experience() {}
        
        public Experience(String company, String position, String duration, String location, String description) {
            this.company = company;
            this.position = position;
            this.duration = duration;
            this.location = location;
            this.description = description;
        }
        
        // Getters and Setters
        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }
        
        public String getPosition() { return position; }
        public void setPosition(String position) { this.position = position; }
        
        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }
        
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
    
    // Update last modified timestamp
    public void updateLastModified() {
        this.lastModified = LocalDateTime.now();
    }
    
    // Get formatted timestamps
    public String getFormattedCreatedAt() {
        return createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    
    public String getFormattedLastModified() {
        return lastModified.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    
    // Main getters and setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }
    
    public String getGithubUrl() { return githubUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }
    
    public String getPortfolioUrl() { return portfolioUrl; }
    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }
    
    public String getSelectedRole() { return selectedRole; }
    public void setSelectedRole(String selectedRole) { this.selectedRole = selectedRole; }
    
    public String getObjective() { return objective; }
    public void setObjective(String objective) { this.objective = objective; }
    
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    
    public List<Education> getEducationList() { return educationList; }
    public void setEducationList(List<Education> educationList) { this.educationList = educationList; }
    
    public List<String> getTechnicalSkills() { return technicalSkills; }
    public void setTechnicalSkills(List<String> technicalSkills) { this.technicalSkills = technicalSkills; }
    
    public List<String> getSoftSkills() { return softSkills; }
    public void setSoftSkills(List<String> softSkills) { this.softSkills = softSkills; }
    
    public List<Project> getProjects() { return projects; }
    public void setProjects(List<Project> projects) { this.projects = projects; }
    
    public List<Experience> getExperiences() { return experiences; }
    public void setExperiences(List<Experience> experiences) { this.experiences = experiences; }
    
    public List<String> getAchievements() { return achievements; }
    public void setAchievements(List<String> achievements) { this.achievements = achievements; }
    
    public List<String> getCertifications() { return certifications; }
    public void setCertifications(List<String> certifications) { this.certifications = certifications; }
    
    public List<String> getLanguages() { return languages; }
    public void setLanguages(List<String> languages) { this.languages = languages; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }
    
    public String getTemplateSelected() { return templateSelected; }
    public void setTemplateSelected(String templateSelected) { this.templateSelected = templateSelected; }
}