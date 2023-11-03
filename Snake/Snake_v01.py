import random
import pygame
import tkinter as tk
from tkinter import messagebox


class Cube(object):
    rows = 20
    w = 500

    def __init__(self, start, color=(255, 0, 0)):
        self.pos = start
        self.dir_nx = 1
        self.dir_ny = 0
        self.color = color

    def move(self, dir_nx, dir_ny):
        self.dir_nx = dir_nx
        self.dir_ny = dir_ny
        self.pos = (self.pos[0] + self.dir_nx, self.pos[1] + self.dir_ny)

    def draw(self, surface, eyes=False):
        dis = self.w // self.rows
        i = self.pos[0]
        j = self.pos[1]

        pygame.draw.rect(surface, self.color, (i * dis + 1, j * dis + 1, dis - 2, dis - 2))
        if eyes:
            centre = dis // 2
            radius = 3
            circle_middle = (i * dis + centre - radius, j * dis + 8)
            circle_middle2 = (i * dis + dis - radius * 2, j * dis + 8)
            pygame.draw.circle(surface, (0, 0, 0), circle_middle, radius)
            pygame.draw.circle(surface, (0, 0, 0), circle_middle2, radius)


class Snake(object):
    body = []
    turns = {}

    def __init__(self, color, pos):
        self.color = color
        self.head = Cube(pos)
        self.body.append(self.head)
        self.dir_nx = 0
        self.dir_ny = 1

    def move(self):
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()

        keys = pygame.key.get_pressed()

        for key in keys:
            if keys[pygame.K_LEFT]:
                self.dir_nx = -1
                self.dir_ny = 0
                self.turns[self.head.pos[:]] = [self.dir_nx, self.dir_ny]
            elif keys[pygame.K_RIGHT]:
                self.dir_nx = 1
                self.dir_ny = 0
                self.turns[self.head.pos[:]] = [self.dir_nx, self.dir_ny]
            elif keys[pygame.K_UP]:
                self.dir_nx = 0
                self.dir_ny = -1
                self.turns[self.head.pos[:]] = [self.dir_nx, self.dir_ny]
            elif keys[pygame.K_DOWN]:
                self.dir_nx = 0
                self.dir_ny = 1
                self.turns[self.head.pos[:]] = [self.dir_nx, self.dir_ny]

        for i, c in enumerate(self.body):
            p = c.pos[:]
            if p in self.turns:
                turn = self.turns[p]
                c.move(turn[0], turn[1])
                if i == len(self.body)-1:
                    self.turns.pop(p)
            else:
                if c.dir_nx == -1 and c.pos[0] <= 0:
                    c.pos = (c.rows-1, c.pos[1])
                elif c.dir_nx == 1 and c.pos[0] >= c.rows - 1:
                    c.pos = (0, c.pos[1])
                elif c.dir_ny == 1 and c.pos[1] >= c.rows - 1:
                    c.pos = (c.pos[0], 0)
                elif c.dir_ny == - 1 and c.pos[1] <= 0:
                    c.pos = (c.pos[0], c.rows - 1)
                else:
                    c.move(c.dir_nx, c.dir_ny)

    def reset(self, pos):
        self.head = Cube(pos)
        self.body = []
        self.body.append(self.head)
        self.turns = {}
        self.dir_nx = 0
        self.dir_ny = 1

    def add_cube(self):
        tail = self.body[-1]
        dx, dy = tail.dir_nx, tail.dir_ny

        if dx == 1 and dy == 0:
            self.body.append(Cube((tail.pos[0] - 1, tail.pos[1])))
        elif dx == -1 and dy == 0:
            self.body.append(Cube((tail.pos[0] + 1, tail.pos[1])))
        elif dx == 0 and dy == 1:
            self.body.append(Cube((tail.pos[0], tail.pos[1] - 1)))
        elif dx == 0 and dy == -1:
            self.body.append(Cube((tail.pos[0], tail.pos[1] + 1)))

        self.body[-1].dir_nx = dx
        self.body[-1].dir_ny = dy


    def draw(self, surface):
        for i, c in enumerate(self.body):
            if i == 0:
                c.draw(surface, True)
            else:
                c.draw(surface)


def draw_grid(w, rows, surface):
    size_btwn = w // rows

    x = 0
    y = 0
    for i in range(rows):
        x = x + size_btwn
        y = y + size_btwn

        pygame.draw.line(surface, (255, 255, 255), (x, 0), (x, w))
        pygame.draw.line(surface, (255, 255, 255), (0, y), (w, y))


def redraw_window(surface):
    global rows, width, s, snack
    surface.fill((0, 0, 0))
    s.draw(surface)
    snack.draw(surface)
    draw_grid(width, rows, surface)
    pygame.display.update()


def random_snack(rows, item):
    positions = item.body
    while True:
        x = random.randrange(rows)
        y = random.randrange(rows)
        if len(list(filter(lambda z:z.pos == (x, y), positions))) > 0:
            continue
        else:
            break

    return x, y


def message_box(subject, content):
    root = tk.Tk()
    root.attributes("-topmost", True)
    root.withdraw()
    messagebox.showinfo(subject, content)
    try:
        root.destroy()
    except:
        pass


def main():
    global width, rows, s, snack
    width = 500
    rows = 20
    win = pygame.display.set_mode((width, width))
    s = Snake((255, 0, 0), (10,10))
    snack = Cube(random_snack(rows, s), color=(0, 255, 0))
    flag = True

    clock = pygame.time.Clock()

    while flag:
        pygame.time.delay(50)
        clock.tick(10)
        s.move()
        if s.body[0].pos == snack.pos:
            s.add_cube()
            snack = Cube(random_snack(rows, s), color=(0, 255, 0))

        for x in range(len(s.body)):
            if s.body[x].pos in list(map(lambda z: z.pos, s.body[x+1:])):
                print('Score:' + str(len(s.body)))
                message_box("You lost!", "Play again...")
                s.reset((10, 10))
                break



        redraw_window(win)

    pass


main()
