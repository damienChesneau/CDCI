import React, {Component} from 'react'

class Projects extends Component {

    state = {
        affectProjectName: true
    }

    render() {
        return (
            <div>
                <div>
                    <p>Manage a new project.</p>
                    <input type="text"/>
                </div>
            </div>
        )
    }
}

export default (Projects)
