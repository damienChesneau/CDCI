import React, {Component} from 'react'
import {Route, Switch} from 'react-router-dom'

import {Home, NotFound, Builds, Projects} from '../'

class App extends Component {
    render() {
        return (
            <div>
                <ul className="nav nav-tabs" id="myTab" role="tablist">
                    <li className="nav-item">
                        <a className="nav-link active" id="home-tab" data-toggle="tab" href="/" role="tab"
                           aria-controls="home" aria-selected="true">Home</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" id="profile-tab" data-toggle="tab" href="/builds" role="tab"
                           aria-controls="profile" aria-selected="false">Builds</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" id="messages-tab" data-toggle="tab" href="/projects" role="tab"
                           aria-controls="messages" aria-selected="false">Project</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" id="settings-tab" data-toggle="tab" href="#settings" role="tab"
                           aria-controls="settings" aria-selected="false">Profile</a>
                    </li>
                </ul>

                <div className="app">
                    <div className="container-fluid">
                        <Switch>
                            <Route exact path="/" component={Home}/>
                            <Route exact path="/builds" component={Builds}/>
                            <Route exact path="/projects" component={Projects}/>
                            <Route component={NotFound}/>
                        </Switch>
                    </div>
                </div>
            </div>
        )
    }
}

export default App
