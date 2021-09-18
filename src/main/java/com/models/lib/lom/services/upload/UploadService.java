package com.models.lib.lom.services.upload;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.models.lib.lom.services.files.Files;
import com.models.lib.lom.services.files.FilesService;

@Service
public class UploadService {

	static final String FOLDER = "D:\\Development\\arslab-services-v2\\files\\";
	
	private final FilesService fService ;

    @Autowired
	public UploadService(FilesService fService) {
		this.fService = fService;
	}
	
	public List<Files> parse(String db_files) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
	    CollectionType clss = mapper.getTypeFactory().constructCollectionType(List.class, Files.class);

	    return mapper.readValue(db_files, clss);
	}
	
	public String validate(List<MultipartFile> files, List<Files> db_files) {
		if (files.isEmpty()) return "Empty list of files provided.";

		if (db_files.isEmpty()) return "Empty list of database files provided.";

		if (db_files.size() != files.size()) return "Length mismatch between the ids and files provided.";

		for (int i = 0; i < files.size(); i++) { 
			if (!files.get(i).getOriginalFilename().equals(db_files.get(i).getName())) {
				return "File name mismatch between between files provided.";
			}
		}
		
		return null;
	}
	
    public void copyFile(MultipartFile f, BigInteger id) throws IOException {
		String name = FOLDER + id.toString() + "_" + f.getOriginalFilename();
		
		java.nio.file.Files.copy(f.getInputStream(), Paths.get(name), StandardCopyOption.REPLACE_EXISTING);
    }
    
    public void upload(List<MultipartFile> files, List<Files> db_files) throws IOException {
		List<BigInteger> ids = this.fService.create(db_files).stream().map(id -> (BigInteger)id).collect(Collectors.toList());

		for (int i = 0; i < files.size(); i++) {
			this.copyFile(files.get(i), ids.get(i));
		}
    }
}
