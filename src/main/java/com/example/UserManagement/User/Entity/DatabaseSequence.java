package com.example.UserManagement.User.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Document(collection = "UserSequence")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseSequence {
 
    
    @Id
    private String id;
    private int seqNo;

    public String getId() {
		return id;
	}
 
	public void setId(String id) {
		this.id = id;
	}
 
	public int getSeqNo() {
		return seqNo;
	}
 
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
}

