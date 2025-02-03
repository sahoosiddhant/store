package com.store.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "store_failure_push")
@NoArgsConstructor
public class StoreFailure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String store_key;
    @Embedded
    private ValueFail value;

    public StoreFailure(Long id, String store_key, ValueFail value) {
        this.id = id;
        this.store_key = store_key;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStore_key() {
        return store_key;
    }

    public void setStore_key(String store_key) {
        this.store_key = store_key;
    }

    public ValueFail getValue() {
        return value;
    }

    public void setValue(ValueFail value) {
        this.value = value;
    }

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ValueFail {

        @Embedded
        private MessageFail message;
        @Embedded
        private DestinationFail Destination;
        @Embedded
        private MetadataFail metadata;

        public ValueFail(MessageFail message, DestinationFail destination, MetadataFail metadata) {
            this.message = message;
            Destination = destination;
            this.metadata = metadata;
        }

        @Embeddable
        public static class MessageFail{
            public MessageFail(String title, String body) {
                this.title = title;
                this.body = body;
            }

            private String title;
            private String body;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }
        }

        @Embeddable
        @NoArgsConstructor
        public static class DestinationFail{
            public DestinationFail(String store, String department, String jobcode, String role, String salesId, String sourceSystem) {
                this.store = store;
                this.department = department;
                this.jobcode = jobcode;
                this.role = role;
                this.salesId = salesId;
                this.sourceSystem = sourceSystem;
            }

            private String store;
            private String department;
            private String jobcode;
            private String role;
            private String salesId;
            private String sourceSystem;

            public String getStore() {
                return store;
            }

            public void setStore(String store) {
                this.store = store;
            }

            public String getDepartment() {
                return department;
            }

            public void setDepartment(String department) {
                this.department = department;
            }

            public String getJobcode() {
                return jobcode;
            }

            public void setJobcode(String jobcode) {
                this.jobcode = jobcode;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public String getSalesId() {
                return salesId;
            }

            public void setSalesId(String salesId) {
                this.salesId = salesId;
            }

            public String getSourceSystem() {
                return sourceSystem;
            }

            public void setSourceSystem(String sourceSystem) {
                this.sourceSystem = sourceSystem;
            }
        }

        @Embeddable
        @NoArgsConstructor
        public static class MetadataFail{

            public MetadataFail(String priority, String timestamp) {
                this.priority = priority;
                this.timestamp = timestamp;
            }

            private String priority;
            private String timestamp;

            public String getPriority() {
                return priority;
            }

            public void setPriority(String priority) {
                this.priority = priority;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }
        }

    }

}
