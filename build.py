import sys
import os
import re
import subprocess, shlex

oprint = print
plevel = 0
def print(*args):
	if plevel>0: args = ['  '*(plevel-1)+' ']+list(args)
	oprint(*args)

workspace = os.getenv('WORKSPACE', os.path.dirname(os.path.abspath(__file__)))
print('Workspace:', workspace)

def getBranchName():
	print('Getting branch')
	
	cmd = "git branch"
	process = subprocess.Popen(cmdsplit(cmd), stdout=subprocess.PIPE, stderr=subprocess.STDOUT, bufsize=-1)
	branches, _ = process.communicate()
	branches = branches.decode("utf-8")
	c = re.search(r'\* (.*)\n', branches).groups()
	
	return c[0]
	
def importMCP():
	global mcp_dir
	//TODO Get forge stuff
	mcp_dir = os.path.abspath('forge/mcp')
	sys.path.append(mcp_dir)

def getVersion():
	from runtime.commands import Commands
	Commands._version_config = os.path.join(mcp_dir,Commands._version_config)
	
	print("Obtaining version information from git")
	cmd = "git describe --long --match='[^(jenkins)]*'"
	try:
		process = subprocess.Popen(cmdsplit(cmd), stdout=subprocess.PIPE, stderr=subprocess.STDOUT, bufsize=-1)
		vers, _ = process.communicate()
	except OSError:
		print("Git not found")
		vers="v1.0-0-deadbeef"
	(major,minor,info,rev,githash)=re.match("v(\d+).(\d+)(-.*)?-(\d+)-(.*)",vers).groups()
	if not info: info=""

	(mcpversion,mcversion,mcserverversion) = re.match("[.\w]+ \(data: ([.\w]+), client: ([.\w.]+), server: ([.\w.]+)\)",Commands.fullversion()).groups()

	verData = {
		"Boxes.build.major.number": major,
		"Boxes.build.minor.number": minor,
		"Boxes.build.info": info,
		"Boxes.build.revision.number": rev,
		"Boxes.build.number": os.getenv('BUILD_NUMBER', 1),
		
		"Boxes.build.githash": githash,
		"Boxes.build.mcpversion": mcpversion,
		"Boxes.build.mcversion": mcversion,
	}

	print("Version information: Boxes %s.%s.%s%s using MCP %s for %s" % (major, minor, rev, info, mcpversion, mcversion))
	
def cmdsplit(args):
	if os.sep == '\\':
		args = args.replace('\\', '\\\\')
	return shlex.split(args)
	
print(getBranchName())