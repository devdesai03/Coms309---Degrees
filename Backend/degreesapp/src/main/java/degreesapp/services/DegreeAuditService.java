package degreesapp.services;

import degreesapp.models.Student;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public interface DegreeAuditService {
    abstract class DegreeAuditOptions {
        public abstract Student student();

        public boolean includeInProgressCourses() {
            return true;
        }
    }

    void writeDegreeAudit(PrintWriter out, DegreeAuditOptions options);
}
