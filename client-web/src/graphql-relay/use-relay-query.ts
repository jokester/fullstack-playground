import {createContext} from "react";

export interface RelayDeps {

}

const RelayContext = createContext<Promise<RelayDeps>>(null!)

export const RelayProvider = 1