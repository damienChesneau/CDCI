import * as types from '../constants/types'
import EventBus from 'vertx3-eventbus-client';


export const getBuilds = () =>
    dispatch =>
        fetch(`http://10.157.56.160:8002/builds`, {
            mode: 'cors'
        }).then(res => (res.text())).then(response => {
            let va = JSON.parse(response);
            dispatch({
                type: types.FETCH_BUILDS,
                payload: va.builds
            })
        })
export const getBuilds2 = (props) =>
    fetch(`http://10.157.56.160:8002/builds`, {
        mode: 'cors'
    }).then(res => (res.text())).then(response => {
        let va = JSON.parse(response);
        props.handleAction(va.builds);
    })

export const eventProc = (props) => {
    var eventBus = new EventBus("http://10.157.56.160:8002/eventbus/");

    eventBus.onopen = function () {
        eventBus.registerHandler("main-builds", function (error, message) {
            props.handler(JSON.parse(message.body));
        });
    }
}