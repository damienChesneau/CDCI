import React from 'react'
import Build from "../Build";

const BuildList = ({builds}) =>
    <div className="row">
        {builds.map(build =>
                <Build key={build.id} build={build}/>
        )}
    </div>;

export default BuildList
