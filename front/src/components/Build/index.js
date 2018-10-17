import React from 'react'
import './styles.css'

const Build = ({build}) =>
        <div>
            <td className={build.color}>{build.name}</td>
            <td>{build.description}</td>
            <td>Console</td>
        </div>;

export default Build
