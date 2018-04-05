import os
import glob
import subprocess
import shutil
import semantic_version
import argparse
from contextlib import contextmanager

app_name = "pokerpals"


@contextmanager
def cd(path):
    old_dir = os.getcwd()
    os.chdir(path)
    try:
        yield
    finally:
        os.chdir(old_dir)


def go_to_package_root():
    os.chdir(os.path.join(os.getcwd(), os.path.dirname(__file__), ".."))


def build_frontend():
    print("Building frontend...")

    with cd("client"):
        shutil.rmtree("dist", ignore_errors=True)
        subprocess.run([shutil.which("yarn"), "build"], check=True)


def copy_frontend_to_backend():
    print("Copying frontend to backend...")

    shutil.rmtree("api/src/main/resources/public", ignore_errors=True)
    shutil.copytree("client/dist", "api/src/main/resources/public")


def build_backend():
    print("Building backend...")
    with cd("api"):
        executable = "./gradlew"

        if os.name == 'nt':
            executable = "gradlew.bat"

        subprocess.run([executable, "bootRepackage"], check=True)


def copy_backend_to_packaging():
    print("copying jar to packaging...")
    jar = glob.glob("api/build/libs/*.jar")[0]
    shutil.copy2(jar, "deployment/package")


def copy_aws_config_to_packaging():
    print("copying aws config to packaging...")
    with cd("deployment"):
        shutil.copytree(".ebextensions", "package/.ebextensions")


def package(filename):
    print("packaging into ", filename, "...")
    with cd("deployment"):
        shutil.make_archive(filename, "zip", "package")


def find_latest_version():
    files = glob.glob("deployment/*.zip")
    versions = []
    for f in files:
        no_extension = os.path.splitext(os.path.basename(f))[0]
        sections = no_extension.partition("-")
        versions.append(semantic_version.Version(sections[-1]))

    versions.append(semantic_version.Version("0.0.3"))

    return max(versions)


def build(name):
    print("Creating aws package ", name+".zip...")

    build_frontend()
    copy_frontend_to_backend()
    build_backend()
    copy_backend_to_packaging()
    copy_aws_config_to_packaging()

    package(name)


def main():
    parser = argparse.ArgumentParser(
        description="build a elastic beanstalk package for pokerpals")
    parser.add_argument("type", choices=["major", "minor", "patch"],
                        help="the type is the type of upgrade that took place")

    args = parser.parse_args()

    go_to_package_root()

    version = find_latest_version()
    if args.type == "major":
        version = version.next_major()
    elif args.type == "minor":
        version = version.next_minor()
    else:
        version = version.next_patch()

    name = app_name + "-" + str(version)

    build(name)


if __name__ == '__main__':
    main()
