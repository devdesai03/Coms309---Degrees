package degreesapp.services;

import degreesapp.models.Prerequisites;
import degreesapp.repositories.PrerequisiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PrerequisiteServiceImpl implements PrerequisiteService {
    @Autowired
    private PrerequisiteRepository prerequisiteRepository;

    @Override
    public boolean hasCyclicDependency(Prerequisites prerequisite) {
        // Perform a depth-first search (DFS) to detect cycles for all existing prerequisites
        Set<Long> visited = new HashSet<>();
        Set<Long> currentlyVisiting = new HashSet<>();

        List<Prerequisites> allPrerequisites = prerequisiteRepository.findAll();
        for ( Prerequisites prerequisites : allPrerequisites ) {
            if ( !visited.contains(prerequisites.getPreCourse().getId()) ) {
                if ( dfsHasCyclicDependency(prerequisite.getPreCourse().getId() , visited , currentlyVisiting) ) {
                    return true; // Cycle detected
                }
            }
        }
        return false; // No cycles detected
    }


    private boolean dfsHasCyclicDependency(Long courseId , Set<Long> visited , Set<Long> currentlyVisiting) {
        visited.add(courseId);
        currentlyVisiting.add(courseId);

        List<Prerequisites> prerequisites = prerequisiteRepository.findByPreCourse_Id(courseId);

        for ( Prerequisites prereq : prerequisites ) {
            Long nextCourseId = prereq.getPostCourse().getId();

            if ( currentlyVisiting.contains(nextCourseId) || (visited.contains(nextCourseId) && dfsHasCyclicDependency(nextCourseId , visited , currentlyVisiting)) ) {
                return true; // Cycle detected
            }
        }

        currentlyVisiting.remove(courseId);
        return false;
    }


}
