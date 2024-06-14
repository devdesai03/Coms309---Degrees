package degreesapp.services;


import degreesapp.models.Prerequisites;

import java.util.List;

public interface PrerequisiteService {
    public boolean hasCyclicDependency(Prerequisites prerequisite);
}
