package com.lifecycle.services.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class ModelMeta {

    private UUID uuid;
    private String name;
    private String description;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date created;
    
    public Boolean CompareName(ModelMeta m) {
    	return this.getName().equals(m.getName());
    }
    
    public Boolean CompareUuid(ModelMeta m) {
    	return this.getUuid().equals(m.getUuid());
    }
	
	public ObjectNode Json() {
    	final JsonNodeFactory factory = JsonNodeFactory.instance;
        
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    	return factory.objectNode()
					  .put("uuid", this.uuid.toString())
					  .put("name", this.name)
					  .put("description", this.description)
					  .put("created", df.format(this.created));
	}
}
