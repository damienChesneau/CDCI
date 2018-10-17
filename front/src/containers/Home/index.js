import React, {Component} from 'react'
import {bindActionCreators} from 'redux'
import {BuildList} from '../../components'
import * as buildsActions from '../../actions/builds'
import {connect} from 'react-redux'

class Home extends Component {
    componentWillMount()
    {
        this.props.actions.getBuilds();
    }

    render()
    {
        const {builds, actions} = this.props;
        return (
                <div className="container">
                    <table className="table table-striped">
                        <BuildList builds={builds}/>
                    </table>
                </div>
        )
    }
}

export default connect(
        state => ({
            builds: state.product.builds
        }),
        dispatch => ({
            actions: bindActionCreators(buildsActions, dispatch)
        })
)(Home)
