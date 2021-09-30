package de.htw.ai.momotarian.model;

import javax.persistence.*;

@Entity
@Table(name="User")
public class User {
    private String userId;
    private String password;
    private String firstname;
    private String lastname;
    private String token;

    public String toString() {
        return "User [id=, userId=" + this.userId + ", firstname=" + this.firstname + ", lastname=" + this.lastname + "]";
    }

    private User(Builder builder) {
        this.userId = builder.userId;
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.password = builder.password;
        this.token = builder.token;
    }

    public User() {
    }

    public User(String userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "userId")
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userid) {
        this.userId = userid;
    }

    @Column(name = "password")
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "firstname")
    public String getFirstname() {
        return this.firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Column(name="lastname")
    public String getLastname() {
        return this.lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Column(name="token")
    public String getToken(){ return this.token;}
    public void setToken(String token) {this.token = token;}

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String userId;
        private String password;
        private String firstname;
        private String lastname;
        private String token;


        private Builder() {
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withFirstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder withLastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder withToken(String token){
            this.token = token;
            return this;
        }
        public User build() {
            return new User(this);
        }
    }
}
