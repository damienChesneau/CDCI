import React, {Component} from 'react'
import './styles.css'

class Build extends Component {
    state = {
        build: [],
        expandConsole: false
    }

    constructor(props) {
        super(props);
        this.state = {
            build: props.build,
            expandConsole: false
        };
    }

    viewConsole() {
        if (this.state.expandConsole === true) {
            this.setState({expandConsole: false})
        } else {
            console.log("Trie")
            this.setState({expandConsole: true})
        }
    }

    getConsoleMessage() {
        if (this.state.expandConsole === true) {
                let consoleLog= this.state.build.console.split("-");
             consoleLog.map(x=>{
                 return  ({x} (<br/>));
            });
        } else if (this.state.expandConsole === false) {
            return <div>Project info...<br/>Build spend 2 hours.</div>
        } else {
            return "Error."
        }
    }

    render(build) {
        return (
            <div className={getMainDivClassName(this.state.build.success ? "green" : "red")} style={{'margin': '14px'}}>
                <div  className="card-header"><div style={{display: 'contents'}}>{this.state.build.project + " " + this.state.build.id} </div>
                    <button type="button" className="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div  className={getBodyDivClassName(this.state.build.success ? "green" : "red")}>
                    <p className="card-text">{this.getConsoleMessage()}</p>
                </div>
                <div className="d-flex justify-content-between align-items-center" style={{padding: '13px'}}>
                    <div className="btn-group">
                        <button type="button" onClick={this.viewConsole.bind(this)}
                                className="btn btn-sm btn-outline-secondary">View
                            console
                        </button>
                        <button type="button" className="btn btn-sm btn-outline-secondary">View project</button>
                    </div>
                    <small className="text-muted">{getDateMessage(this.state.build.timestamp)}</small>
                </div>
            </div>
        )
    }
}


const getMainDivClassName = (color) => {
    if (color === "red") {
        return "card border-danger col-md-3 ";
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

const getDateMessage = (buildTimestamp) => {

    var date1 = new Date(buildTimestamp).getTime();
    var date2 = new Date().getTime()
    var msec = date2 - date1;
    var mins = Math.floor(msec / 60000);
    var hrs = Math.floor(mins / 60);
    var days = Math.floor(hrs / 24);
    var yrs = Math.floor(days / 365);

    let sec = ((msec / 600)).toFixed(0);
    if (sec <= 119) {
        return sec + " " + format(sec, "s") + " ago.";
    } else if (mins < 60) {
        return mins.toFixed(0) + " " + format(mins, "m") + " ago";
    } else if (hrs < 24) {
        return hrs.toFixed(0) + " " + format(hrs, "h") + " ago";
    } else {
        return days.toFixed(0) + " " + format(days, "d") + " ago."
    }
}

function format(number, type) {
    switch (type) {
        case "s":
            return (number > 1 ? "seconds" : "second");
        case "m":
            return number > 1 ? "minutes" : "minute";
        case "h":
            return number > 1 ? "hours" : "hour";
        case "d":
            return number > 1 ? "days" : "day";
    }
}

export default Build
