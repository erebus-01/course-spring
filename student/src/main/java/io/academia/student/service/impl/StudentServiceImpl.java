package io.academia.student.service.impl;

import io.academia.student.dto.AddressRequest;
import io.academia.student.dto.AddressResponse;
import io.academia.student.dto.StudentRequest;
import io.academia.student.dto.StudentResponse;
import io.academia.student.model.Address;
import io.academia.student.model.Student;
import io.academia.student.repository.AddressRepository;
import io.academia.student.repository.StudentRepository;
import io.academia.student.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final AddressRepository addressRepository;


    @Override
    public StudentResponse createStudent(StudentRequest studentRequest, MultipartFile mainImage) throws IOException {
        Student student = mapStudentRequestToEntity(studentRequest, mainImage);
        Address address = mapAddressRequestToEntity(studentRequest.getAddress());
        student.setAddress(address);
        address.setStudent(student);
        student = studentRepository.save(student);
        return mapStudentEntityToResponse(student);
    }

    @Override
    public StudentResponse updateStudent(UUID studentId, StudentRequest student, MultipartFile mainImage) throws IOException {
        Student existingStudent = studentRepository.findById(studentId).orElseThrow(EntityNotFoundException::new);
        existingStudent.setLinksYourWebsite(student.getLinksYourWebsite());
        existingStudent.setLinksYourFacebook(student.getLinksYourFacebook());
        existingStudent.setLinksYourTwitter(student.getLinksYourTwitter());
        existingStudent.setLinksYourLinkedIn(student.getLinksYourLinkedIn());
        existingStudent.setLinksYourYoutube(student.getLinksYourYoutube());

        if (mainImage != null && !mainImage.isEmpty()) {
            String newName = RenameFileImage(student.getPhoto());
            String fileDownloadUri = "http://localhost:8503/files/" + newName;

            existingStudent.setPhoto(mainImage.getBytes());
            existingStudent.setNamePhoto(newName);
            existingStudent.setUrlPhoto(fileDownloadUri);
            existingStudent.setTypePhoto(mainImage.getContentType());
        }

        Address existingAddress = existingStudent.getAddress();
        Address updatedAddress = mapAddressRequestToEntity(student.getAddress());

        existingAddress.setStreetAddress(updatedAddress.getStreetAddress());
        existingAddress.setCity(updatedAddress.getCity());
        existingAddress.setStateProvince(updatedAddress.getStateProvince());
        existingAddress.setPostalCode(updatedAddress.getPostalCode());
        existingAddress.setCountry(updatedAddress.getCountry());
        existingAddress.setRecipientName(updatedAddress.getRecipientName());

        existingStudent = studentRepository.save(existingStudent);

        return mapStudentEntityToResponse(existingStudent);
    }

    @Override
    public void deleteStudent(UUID studentId) {
        studentRepository.deleteById(studentId);
    }

    @Override
    public StudentResponse getStudentById(UUID studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(EntityNotFoundException::new);
        return mapStudentEntityToResponse(student);
    }

    @Override
    public Student findByNamePhoto(String namePhoto) {
        return studentRepository.findByNamePhoto(namePhoto);
    }

    private Student mapStudentRequestToEntity(StudentRequest studentRequest, MultipartFile mainImage) throws IOException {
        Student student = new Student();
        student.setLinksYourWebsite(studentRequest.getLinksYourWebsite());
        student.setLinksYourFacebook(studentRequest.getLinksYourFacebook());
        student.setLinksYourTwitter(studentRequest.getLinksYourTwitter());
        student.setLinksYourLinkedIn(studentRequest.getLinksYourLinkedIn());
        student.setLinksYourYoutube(studentRequest.getLinksYourYoutube());

        if(mainImage != null && !mainImage.isEmpty()) {
            String newName = RenameFileImage(studentRequest.getPhoto());
            String fileDownloadUri = "http://localhost:8503/files/" + newName;

            student.setPhoto(mainImage.getBytes());
            student.setNamePhoto(newName);
            student.setUrlPhoto(fileDownloadUri);
            student.setTypePhoto(mainImage.getContentType());
        }

        return student;
    }

    private Address mapAddressRequestToEntity(Address addressRequest) {
        Address address = new Address();
        address.setStreetAddress(addressRequest.getStreetAddress());
        address.setCity(addressRequest.getCity());
        address.setStateProvince(addressRequest.getStateProvince());
        address.setPostalCode(addressRequest.getPostalCode());
        address.setCountry(addressRequest.getCountry());
        address.setRecipientName(addressRequest.getRecipientName());

        return address;
    }

    private StudentResponse mapStudentEntityToResponse(Student student) {
        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setId(student.getId());
        studentResponse.setLinksYourWebsite(student.getLinksYourWebsite());
        studentResponse.setLinksYourFacebook(student.getLinksYourFacebook());
        studentResponse.setLinksYourTwitter(student.getLinksYourTwitter());
        studentResponse.setLinksYourLinkedIn(student.getLinksYourLinkedIn());
        studentResponse.setLinksYourYoutube(student.getLinksYourYoutube());
        studentResponse.setUrlPhoto(student.getUrlPhoto());
        studentResponse.setCreateAt(student.getCreateAt());
        studentResponse.setUpdateAt(student.getUpdateAt());

        if (student.getAddress() != null) {
            Address address = student.getAddress();
            AddressResponse addressResponse = new AddressResponse();
            addressResponse.setId(address.getId());
            addressResponse.setStreetAddress(address.getStreetAddress());
            addressResponse.setCity(address.getCity());
            addressResponse.setStateProvince(address.getStateProvince());
            addressResponse.setPostalCode(address.getPostalCode());
            addressResponse.setCountry(address.getCountry());
            addressResponse.setRecipientName(address.getRecipientName());
            studentResponse.setAddress(addressResponse);
        } else {
            studentResponse.setAddress(null);
        }

        return studentResponse;
    }

    private static String RenameFileImage(MultipartFile mainImage) {
        String randomFileName = UUID.randomUUID().toString();

        String originalFileName = mainImage.getOriginalFilename();
        assert originalFileName != null;
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        return randomFileName + fileExtension;
    }

}
