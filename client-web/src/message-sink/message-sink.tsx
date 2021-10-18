import {FC, useEffect, useRef, useState} from "react";
import useConstant from "use-constant";
import {webSocket, WebSocketSubject} from "rxjs/webSocket";
import {Json, } from "fp-ts/Json";

export const MessageSinkPicker: FC<{onNameDetermined?(name: string): void}> = () => {
    const [sinkName, setSinkName] = useState<null | string>(null)

    return null
}

export const MessageSinkDemo: FC<{onClose?(): void, name: string}> = props => {
    const msgLogCount = useRef(100)



    return null

}

export const MessageSinkWsDemo: FC<{wsUrl: string}> = props => {
    interface SocketEntities {
        subject: WebSocketSubject<string>
        isOpen: boolean
    }
    const [s, setS] = useState<null | SocketEntities>(null)

    useEffect(() => {
        setS(null)
        const subject = webSocket<string>({url: props.wsUrl, serializer: (value) => value, deserializer: e => e.data as string});
        // TODO: xxx
    }, [props.wsUrl])

    return null;
}
