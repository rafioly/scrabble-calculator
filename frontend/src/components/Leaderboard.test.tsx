import { render, screen } from '@testing-library/react';
import Leaderboard from './Leaderboard';

test('renders leaderboard with scores', () => {
  // Arrange
  const mockLeaderboard = [
    { word: 'HELLO', score: 8 },
    { word: 'WORLD', score: 9 },
  ];

  // Act
  render(<Leaderboard isOpen={true} onClose={() => {}} leaderboard={mockLeaderboard} />);

  // Assert
  expect(screen.getByText(/Top 10 Scores/i)).toBeInTheDocument();
  expect(screen.getByText(/HELLO/i)).toBeInTheDocument();
  expect(screen.getByText(/8/i)).toBeInTheDocument();
  expect(screen.getByText(/WORLD/i)).toBeInTheDocument();
  expect(screen.getByText(/9/i)).toBeInTheDocument();
});

test('renders empty leaderboard message', () => {
  // Arrange
  render(<Leaderboard isOpen={true} onClose={() => {}} leaderboard={[]} />);

  // Act
  const emptyMessage = screen.getByText(/No scores yet/i);

  // Assert
  expect(emptyMessage).toBeInTheDocument();
});
