# Stacked List Demo Application
## Overview
This simple demo application illustrates a stacked list behavior with two states: collapsed and expanded. In this implementation, when the next item in the stack becomes visible, the previous items are displayed as collapsed. Furthermore, clicking on any collapsed item expands it, hiding the other items. 

[![Watch the video](https://github.com/amitseervi/StackedList/blob/main/art/000.png)](https://github.com/amitseervi/StackedList/blob/main/art/recording.mp4)

## Features
- Stacked list view with collapsed and expanded states.
- Smooth transition animations between states.
- Responsive design for optimal user experience.
- Easy-to-understand code structure for customization.
- MVVM design pattern

## Getting Started

```
git clone https://github.com/amitseervi/StackedList.git
cd StackedList
```

## Usage
open the application code in your Android studio. click on run.

```
<com.amit.stackedlist.view.StackContainer
        app:stackAnimationDuration="300">

        <com.amit.stackedlist.view.StackItemView>

            <com.amit.stackedlist.view.CollapsedViewContainer>

               <!--- include your layout for showing Collapsed view --->
            </com.amit.stackedlist.view.CollapsedViewContainer>

            <com.amit.stackedlist.view.ExpandedViewContainer>

                <!--- include your layout for showing expanded view --->
            </com.amit.stackedlist.view.ExpandedViewContainer>
        </com.amit.stackedlist.view.StackItemView>

        <com.amit.stackedlist.view.StackItemView>

            <com.amit.stackedlist.view.CollapsedViewContainer>

                <!--- include your layout for showing collapsed view --->
            </com.amit.stackedlist.view.CollapsedViewContainer>

            <com.amit.stackedlist.view.ExpandedViewContainer>

                <!--- include your layout for showing expanded view --->
            </com.amit.stackedlist.view.ExpandedViewContainer>
        </com.amit.stackedlist.view.StackItemView>


        <com.amit.stackedlist.view.StackItemView>

            <com.amit.stackedlist.view.ExpandedViewContainer>

                <!--- include your layout for showing expanded view --->

            </com.amit.stackedlist.view.ExpandedViewContainer>
        </com.amit.stackedlist.view.StackItemView>
    </com.amit.stackedlist.view.StackContainer>
```

for the last item in the stackContainer collapsedState view is optional and can be skipped.

## Art
![alt text](https://github.com/amitseervi/StackedList/blob/main/art/000.png)
![alt text](https://github.com/amitseervi/StackedList/blob/main/art/001.png)
![alt text](https://github.com/amitseervi/StackedList/blob/main/art/002.png)
![alt text](https://github.com/amitseervi/StackedList/blob/main/art/003.png)
![alt text](https://github.com/amitseervi/StackedList/blob/main/art/004.png)
![alt text](https://github.com/amitseervi/StackedList/blob/main/art/005.png)
![alt text](https://github.com/amitseervi/StackedList/blob/main/art/006.png)


## Guidelines
- Constraints checks are not added but for the guideline purpose stackContainer can have max 4 stack items and a minimum of 2 stack items.
- Each stack must have two stats collapsed & expanded except for the last stackItem  because that never gets collapsed.
- The developer needs to handle back press explicitly by attaching onBackPressCallback to the activity or fragment lifecycle.
- There are no checks added at the library level to handle out-of-edge cases that need to be tested manually. checks are not added to allow developers to fully utilize the library in unexpected ways.
