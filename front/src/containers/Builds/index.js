import React, {Component} from 'react'
import BuildList from "../../components/BuildList";
import * as buildsActions from '../../actions/builds'

class Builds extends Component {

    state = {
        builds: []
    }

    constructor(props) {
        super(props);
        this.state = {
            builds: []
        };

    }

    handleAction(response) {
        console.log(response);
        this.setState({builds: response});
    }

    componentDidMount() {
        buildsActions.getBuilds2({handleAction: this.handleAction.bind(this), inst: this});
        buildsActions.eventProc({handler: this.updateBuildsState.bind(this)});
    }

    updateBuildsState(build) {
        let builds = this.state.builds;
        builds.push(build);
        this.setState({builds: builds});
    }

    render() {
        return (
            <div >
                <div >
                    <p>Hello mon test</p>
                    {/*<table className="table table-striped">*/}
                        <BuildList builds={this.state.builds}/>
                    {/*</table>*/}
                </div>
            </div>
        )
    }
}

export default (Builds)
