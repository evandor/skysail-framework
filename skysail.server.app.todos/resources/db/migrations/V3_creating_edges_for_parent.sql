select from (
    select @rid as todoId, parent as listId from Todo where IN('todos') is null OR IN('todos').size() = 0
)
let $ce = todosEdgeCreator(listId, todoId)