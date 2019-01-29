import React from 'react'
import Project from "../Project";

const ProjectList = ({projects}) =>
    <div className="row">
        {projects.reverse().map(project =>
            <Project key={project.id} project={project}/>
        )}
    </div>;

export default ProjectList
