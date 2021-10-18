import React from "react";
import {useRouter} from "next/router";

const MessageSinkShowPage: React.FC = () => {
    const router = useRouter()
    const {sinkName} = router.query as { sinkName: string}

    return <div>TODO</div>
}
export default MessageSinkShowPage
