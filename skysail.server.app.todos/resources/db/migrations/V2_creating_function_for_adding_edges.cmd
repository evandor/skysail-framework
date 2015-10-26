
CREATE FUNCTION todosEdgeCreator
"
  var gdb = orient.getGraph();

  if(to != null && to.size() != 0){
    var command = 'create edge todos from ' + from + ' to ' + to;
    gdb.command('sql', command);
  }
  return;
"
PARAMETERS [from,to]
LANGUAGE Javascript