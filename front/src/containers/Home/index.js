import React, {Component} from 'react'
import {bindActionCreators} from 'redux'
import * as buildsActions from '../../actions/builds'
import {connect} from 'react-redux'

class Home extends Component {

    render() {
        return (
            <div>
                <div className="jumbotron">
                <h1 className="display-4">Hello, world!</h1>
                <p className="lead">This is a simple hero unit, a simple jumbotron-style component for calling extra
                attention to featured content or information.</p>
                <a className="btn btn-primary btn-lg" href="test" role="button">Learn more</a>
                </div>
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
