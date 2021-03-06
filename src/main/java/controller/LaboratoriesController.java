package controller;

import model.Laboratory;
import model.Student;
import repository.FileDataPersistence;
import validator.Validator;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Gozner on 4/1/2017.
 */

public class LaboratoriesController {
    private FileDataPersistence studentPersistence = new FileDataPersistence(
            "students.txt");
    private FileDataPersistence laboratoryPersistence = new FileDataPersistence(
            "laboratories.txt");

    public LaboratoriesController(String studentFile, String laboratoryFile) {
        this.studentPersistence = new FileDataPersistence(studentFile);
        this.laboratoryPersistence = new FileDataPersistence(laboratoryFile);
    }

    public boolean saveStudent(Student student) {
        if (Validator.validateStudent(student)) {
            this.studentPersistence.saveStudent(student);
            return true;
        } else {
            return false;
        }
    }

    public boolean saveLaboratory(Laboratory laboratory)throws IOException, ParseException {
        if (Validator.validateLaboratory(laboratory, getallStudents())) {
            this.laboratoryPersistence.saveLaboratory(laboratory);
            return true;
        } else {
            return false;
        }
    }

    public boolean addGrade(String nrReg, String labNumber, float grade)
            throws NumberFormatException, IOException, ParseException {
        if (Validator.validateGrade(grade) && Validator.exitNrReg(nrReg,getallStudents())) {
            this.laboratoryPersistence.addGrade(nrReg, labNumber, grade);
            return true;
        } else {
            return false;
        }
    }

    public List<Student> passedStudents() throws NumberFormatException,
            IOException, ParseException {
        Map<String, List<Laboratory>> laboratoryMap = this.laboratoryPersistence.getLaboratoryMap();
        List<Student> studentsList = studentPersistence.getStudentsList();

        List<Student> passedStudents = new ArrayList<Student>();
        Map.Entry<String, List<Laboratory>> entry;

        Set<Map.Entry<String, List<Laboratory>>> entrySet = laboratoryMap.entrySet();
        Iterator<Map.Entry<String, List<Laboratory>>> iterator = entrySet.iterator();

        while (iterator.hasNext()) {
            entry = iterator.next();
            float midGrade = entry.getValue().get(0).getGrade();
            for (Laboratory laboratory : entry.getValue()) {
                midGrade = (midGrade + laboratory.getGrade()) / 2;
            }
            System.out.println(midGrade);
            if (midGrade >= 5) {
                Student student = new Student();
                student.setRegNumber(entry.getKey());
                int indexOf = studentsList.indexOf(student);
                passedStudents.add(studentsList.get(indexOf));
            }
        }

        return passedStudents;
    }

    public List<Student> getallStudents() throws NumberFormatException,
            IOException, ParseException {
        return studentPersistence.getStudentsList();
    }
}
