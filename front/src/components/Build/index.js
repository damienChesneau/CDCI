import React from 'react'
import './styles.css'

// const Build = ({build}) =>
//     <tr>
//         <td className={build.color}>{build.name}</td>
//         <td>{build.description}</td>
//         <td>Console</td>
//     </tr>
// ;

const Build = ({build}) =>
        <div className={getMainDivClassName(build.color)} style={{'margin': '14px'}}>
            <div className="card-header">{build.name}</div>
            <div className={getBodyDivClassName(build.color)}>
                {/*<h5 className="card-title">{build.color}</h5>*/}
                <p className="card-text">{build.description}</p>
            </div>
            <button type="button" className="btn btn-primary" style={{'margin': '14px'}}>Console</button>
        </div>
    // <tr>
    //     <td className={build.color}>{build.name}</td>
    //     <td>{build.description}</td>
    //     <td>Console</td>
    // </tr>
;

const getMainDivClassName = (color) => {
    if (color == "red") {
        return "card border-danger col-md-3";
    } else {
        return "card border-success col-md-3";
    }
}

const getBodyDivClassName = (color) => {
    if (color == "red") {
        return "card-body text-danger";
    } else {
        return "card-body text-success";
    }
}

export default Build
