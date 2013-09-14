import sys
import os
import re
import subprocess, shlex
import shutil

oprint = print
plevel = 0
def print(*args):
	if plevel>0: args = ['  '*(plevel-1)+' ']+list(args)
	oprint(*args)

def printHeader(npl, hd):
	plevel = npl-1
	print(hd+":")
	plevel = npl

workspace = os.getenv('WORKSPACE', os.path.dirname(os.path.abspath(__file__)))
print('Workspace:', workspace)
extRes = os.path.join(workspace, "../../../userContent")

# Build process functions
def cleanup():
	shutil.rmtree(os.path.join(workspace, "forge"), ignore_errors=True)

def getBranchName():
	global branch
	
	cmd = "git branch"
	process = subprocess.Popen(cmdsplit(cmd), stdout=subprocess.PIPE, stderr=subprocess.STDOUT, bufsize=-1)
	branches, _ = process.communicate()
	branches = branches.decode("utf-8")
	c = re.search(r'\* (.*)\n', branches).groups()
	
	branch = os.getenv("GIT_BRANCH", c[0])
	
def importMCP():
	global mcp_dir
	version = re.search(r'', branch)
	
	if re is not None:
		forge_name = "forge_mc%s" % re.groups()[0]
	else:
		forge_name = "forge"
	
	forge_src = os.path.join(extRes, forge_name)
	shutil.copytree(forge_src, os.path.join(workspace, "forge"))
	
	mcp_dir = os.path.join(workspace, 'forge/mcp')
	sys.path.append(mcp_dir)
	
def getVersion():
	from runtime.commands import Commands
	Commands._version_config = os.path.join(mcp_dir,Commands._version_config)
	
	print("Obtaining version information from git")
	execCmd("git checkout master") # Get tags from the master branch

	try:
		vers = execCmd("git describe --long --match='[^(jenkins)]*'")
	except OSError:
		print("Git not found")
		vers="v1.0-0-deadbeef"
	execCmd("git checkout %s" % branch)
	
	(major,minor,info,rev,githash)=re.match("v(\d+).(\d+)(-.*)?-(\d+)-(.*)",vers).groups()
	if not info: info=""

	(mcpversion,mcversion,mcserverversion) = re.match("[.\w]+ \(data: ([.\w]+), client: ([.\w.]+), server: ([.\w.]+)\)",Commands.fullversion()).groups()

	global verData
	verData = {
		"build.major.number": major,
		"build.minor.number": minor,
		"build.info": info,
		"build.revision.number": rev,
		"build.number": os.getenv('BUILD_NUMBER', 1),
		
		"githash": githash,
		"mcpversion": mcpversion,
		"mcversion": mcversion,
	}

	print("Version information: Boxes %s.%s.%s%s using MCP %s for %s" % (major, minor, rev, info, mcpversion, mcversion))
	
def main():
	printHeader(1, 'Cleaning up workspace')
	cleanup()
	
	printHeader(1, 'Getting current Git branch')
	getBranchName()
	
	printHeader(1, 'Importing Forge and MCP')
	importMCP()
	
	printHeader(1, 'Loading version info from Git')
	getVersion()
	
	printHeader(1, 'Loading locally-built dependencies')
	ext_lib_path = os.path.join(workspace, "libs")
	for lib in os.listdir(ext_lib_path):
		if re.search(r'-%s-'%verData["mcversion"], lib):
			print("Loading dependency: %s" % lib)
			shutil.copy(os.path.join(ext_lib_path, lib), os.path.join(mcp_dir, "lib"))
	
	printHeader(1, 'Loading Source code into MCP')
	mergetree(os.path.join(workspace, "src"), os.path.join(mcp_dir, "src"))
	
	printHeader(1, 'Recompiling')
	from runtime.recompile import recompile
	recompile(None, False, False)
	
	printHeader(1, 'Reobfuscating')
	from runtime.reobfuscate import reobfuscate
	reobfuscate(None, False, True, False, False, False, True)
	
	#TODO Get Zip

# Library Functions

def execCmd(cmd):
	process = subprocess.Popen(cmdsplit(cmd), stdout=subprocess.PIPE, stderr=subprocess.STDOUT, bufsize=-1)
	out, _ = process.communicate()
	return out

def cmdsplit(args):
	if os.sep == '\\':
		args = args.replace('\\', '\\\\')
	return shlex.split(args)

#Taken from: http://stackoverflow.com/questions/7545299/distutil-shutil-copytree
def mergetree(src, dst, verbose=0, symlinks=False):
	if verbose == -1:
		verbose = len(os.path.abspath(dst)) + 1
	names = os.listdir(src)
	cmkdir(dst) # XXX
	errors = []
	for name in names:
		srcname = os.path.join(src, name)
		dstname = os.path.join(dst, name)
		try:
			if symlinks and os.path.islink(srcname):
				linkto = os.readlink(srcname)
				os.symlink(linkto, dstname)
			elif os.path.isdir(srcname):
				mergetree(srcname, dstname, verbose, symlinks)
			else:
				shutil.copy2(srcname, dstname)
				if verbose > 0:
					print(os.path.abspath(dstname)[verbose:])
			# XXX What about devices, sockets etc.?
		except (IOError, os.error) as why:
			errors.append((srcname, dstname, str(why)))
		# catch the Error from the recursive copytree so that we can
		# continue with other files
		except Exception as err:
			errors.extend(err.args[0])
	try:
		shutil.copystat(src, dst)
	except WindowsError:
		# can't copy file access times on Windows
		pass

if __name__ == '__main__':
	main()