import React, {Component} from 'react'
import {Route, Switch} from 'react-router-dom'

import {Home, NotFound, Builds} from '../'
import Build from "../../components/Build";

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
                        <a className="nav-link" id="messages-tab" data-toggle="tab" href="#messages" role="tab"
                           aria-controls="messages" aria-selected="false">Project</a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link" id="settings-tab" data-toggle="tab" href="#settings" role="tab"
                           aria-controls="settings" aria-selected="false">Profile</a>
                    </li>
                </ul>

                {/*<div className="tab-content">*/}
                    {/*<div className="tab-pane active" id="home" role="tabpanel" aria-labelledby="home-tab">...</div>*/}
                    {/*<div className="tab-pane" id="profile" role="tabpanel" aria-labelledby="profile-tab">...</div>*/}
                    {/*<div className="tab-pane" id="messages" role="tabpanel" aria-labelledby="messages-tab">...</div>*/}
                    {/*<div className="tab-pane" id="settings" role="tabpanel" aria-labelledby="settings-tab">...</div>*/}
                {/*</div>*/}
                <div className="app">
                    <div className="container-fluid">
                        <Switch>
                            <Route exact path="/" component={Home}/>
                            <Route exact path="/builds" component={Builds}/>
                            <Route component={NotFound}/>
                        </Switch>
                    </div>
                </div>
            </div>
        )
    }
}

export default App
