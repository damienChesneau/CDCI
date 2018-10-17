import React from 'react'
import Build from "../Build";

const BuildList = ({builds}) =>
        <tbody>
        {builds.map(build =>
                <tr>
                    <Build key={build.id} build={build}/>
                </tr>
        )}
        </tbody>;

export default BuildList
