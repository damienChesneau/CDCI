import * as types from '../constants/types'

export const getBuilds = () =>
        dispatch =>
                fetch(`builds.json`)
                        .then(response => response.json())
                        .then(response => {
                            dispatch({
                                type: types.FETCH_BUILDS,
                                payload: response.builds
                            })
                        })