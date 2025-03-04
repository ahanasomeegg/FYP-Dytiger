export function buildReplyTree(flatReplies) {
    const map = {}
    for (const r of flatReplies) {
        map[r.id] = r
        r.children = []
    }
    const rootReplies = []
    for (const r of flatReplies) {
        if (r.parentId) {
            const parent = map[r.parentId]
            if (parent) {
                parent.children.push(r)
            } else {
                rootReplies.push(r)
            }
        } else {
            rootReplies.push(r)
        }
    }
    return rootReplies
}
