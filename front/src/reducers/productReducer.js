import * as types from '../constants/types'

const INITIAL_STATE = {
    builds: []
};

export default function (state = INITIAL_STATE, action) {
    switch (action.type)
    {
        case types.FETCH_BUILDS:
            return {
                ...state, builds: action.payload.map(builds => ({...builds})
                )
            };
        default:
            return state
    }
}
