package com.learn.demostudentpay.services.ServiceImpl;

import com.learn.demostudentpay.dtos.StudentDTO.StudentResponseDTO;
import com.learn.demostudentpay.entites.Student;
import com.learn.demostudentpay.mappers.StudentMapper;
import com.learn.demostudentpay.repositorys.StudentRepository;
import com.learn.demostudentpay.services.serviceInterface.StudentServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentServiceInterface {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    public List<StudentResponseDTO> getAllStudentsImpl() {
        List<Student> student = studentRepository.findAll();
        return studentMapper.toResponseDTOList(student);
    }

    @Override
    public StudentResponseDTO getStudentByCodeImpl(String code) {
        Student student = studentRepository.findByCode(code);
        return studentMapper.toResponseDTO(student);
    }

    @Override
    public List<StudentResponseDTO> getStudentByProgramIdImpl(String programId) {
        List<Student> studentPgId = studentRepository.findByProgramID(programId);
        return studentMapper.toResponseDTOList(studentPgId);
    }

    @Override
    public ResponseEntity<Resource> getImageByCodeImpl(String code) throws IOException {
        Student student = studentRepository.findByCode(code);

        Path filePath = Paths.get(System.getProperty("user.home"), "edd-demo-yousfiData", "users-photos");
        if (!Files.exists(filePath)) Files.createDirectory(filePath);
        //System.out.println(filePath.toString());

        Path PathFinal = Paths.get(filePath.resolve(student.getPhoto()).toUri());
        //System.out.println(PathFinal+" photoID "+student.getPhoto());

        if (!Files.exists(PathFinal)) {
            throw new FileNotFoundException("Image not found  " + PathFinal);
        }
        Resource resource = new UrlResource(PathFinal.toUri());

        if (!resource.exists()) {
            throw new FileNotFoundException("Image not found  " + PathFinal);
        }

        String contentType = Files.probeContentType(PathFinal);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @Override
    public StudentResponseDTO updateProgramByCodeImpl(String programId, String code) {
        Student student = studentRepository.findByCode(code);
        student.setProgramID(programId);
        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toResponseDTO(updatedStudent);
    }

    @Override
    public StudentResponseDTO createStudentImpl(StudentResponseDTO studentResponseDTO, MultipartFile file) throws IOException {
        // 1. Préparer le dossier
        Path filePath = Paths.get(System.getProperty("user.home"), "edd-demo-yousfiData", "users-photos");
        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath); // plus sûr que createDirectory
        }

        // 2. Récupérer l’extension du fichier
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 3. Générer un nom de fichier unique
        String fileName = UUID.randomUUID() + extension;
        Path destination = filePath.resolve(fileName);
        file.transferTo(destination);

        // 4. Mapper le DTO vers l’entité
        Student student = studentMapper.toResponseEntity(studentResponseDTO);
        student.setPhoto(fileName);


        // 5. Générer un ID unique (si UUID et non auto-généré par DB)
        student.setId(UUID.randomUUID().toString());

        // 6. Sauvegarder en DB
        Student savedStudent = studentRepository.save(student);

        // 7. Retourner le ResponseDTO
        return studentMapper.toResponseDTO(savedStudent);
    }


    /*  Student student = Student.builder()
                .Id(UUID.randomUUID().toString())
                .firstName(firstName)
                .lastName(lastName)
                .code("STU" + UUID.randomUUID())
                .programID(programID)
                .Photo(fileName)
                .build();

        return studentRepository.save(student);
    **/

}
