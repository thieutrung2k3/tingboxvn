package com.kir.commonservice.dto.request;



public class EmailRequest {
      private String to;
      private String subject;
      private String body; // HTML content

      public EmailRequest() { }

      public EmailRequest(String to, String subject, String body) {
            this.to = to;
            this.subject = subject;
            this.body = body;
      }

      public static class Builder {
            private final EmailRequest ins = new EmailRequest();
            public Builder to(String to) {
                  ins.to = to;
                  return this;
            }
            public Builder subject(String subject) {
                  ins.subject = subject;
                  return this;
            }
            public Builder body(String body) {
                  ins.body = body;
                  return this;
            }
            public EmailRequest build() {
                  return ins;
            }
      }
      public static Builder builder() {
            return new Builder();
      }
      public String getTo() {
            return to;
      }
      public void setTo(String to) {
            this.to = to;
      }
      public String getSubject() {
            return subject;
      }
      public void setSubject(String subject) {
            this.subject = subject;
      }
      public String getBody() {
            return body;
      }
      public void setBody(String body) {
            this.body = body;
      }
}
