from setuptools import setup


setup(name='cae-cli',
    version='0.2.9.8',
    license='Apache License',
    author='Carlos Vinicius Da Silva',
    long_description="teste da aplicação ainda",
    long_description_content_type="text/markdown",
    author_email='vini989073599@gmail.com',
    keywords='cae-cli',
    description=u'o cae tem como objetivo facilitar a utilização de projeto com arquitetura limpa',
    packages=['cae'],
    package_data={
          'cae': ['templates/*.txt', '*.json']
    },
    install_requires=[
        'arch-flow>=0.1.8.6',
        'colorama'
      ],
)